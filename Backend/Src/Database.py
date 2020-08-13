import os
import face_recognition
import mysql.connector
from datetime import datetime

list_images = []
list_id_customers = []


def connect_mysql():
    return mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="12345678",
        database="TCC"
    )


def load_database_images():
    print("Criando banco de imagens")
    folder = "../Known_Images/"
    for filename in os.listdir(folder):
        print(filename)
        img = face_recognition.load_image_file(os.path.join(folder, filename))
        if img is not None:
            face_encoding = face_recognition.face_encodings(img)[0]
            list_images.append(face_encoding)


def create_new_customer(name, cpf, birthday, image):
    print("Criando novo cliente")
    db = connect_mysql()
    cursor = db.cursor()

    try:
        cursor.execute("INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES (%s, %s, %s)", (name, cpf, birthday))
        db.commit()

        cursor.execute("SELECT MAX(id) FROM Clientes")
        count_max_id = cursor.fetchone()[0]
        print(count_max_id)

        create_new_customer_image(image, count_max_id)
        list_id_customers.append(name)
        cursor.close()
    except mysql.connector.Error as error:
        print("Failed to insert table {}".format(error))
        return {'status': False}
    finally:
        print("Success to insert a new customer!")
        return {'status': True}


def load_database_customer_id():
    print("Selecionando todos os clientes")
    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute("SELECT id FROM Clientes")

    for x in cursor:
        list_id_customers.append(str(x[0]))
    cursor.close()


def create_new_customer_image(image, count_max_id):
    print("Criando nova imagem")

    file_path = "../Known_Images/" + str(count_max_id) + '.jpg'
    print(file_path)
    with open(file_path, 'wb') as f:
        f.write(image)

    img = face_recognition.load_image_file(file_path)
    new_face_encoding = face_recognition.face_encodings(img)[0]
    list_images.append(new_face_encoding)


def search_customer(id_customer):
    print("procurando cliente")
    sql_query = "SELECT nome FROM Clientes WHERE id = " + id_customer

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        return str(x[0])


def search_last_purchase(id_customer):
    print("procurando ultima compra")
    sql_query = "SELECT MAX(data), valor_total, id FROM Compras WHERE id_cliente = " + id_customer
    retorno = []

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        retorno.append({
            'data': x[0],
            'valor_total': x[1],
            'itens_comprados': search_produtos(x[2])
        })
    print(retorno)
    return retorno


def search_produtos(id_purchase):
    print("procurando ultima compra")
    sql_query = "SELECT Produtos.nome FROM Itens_comprados INNER JOIN Produtos ON Itens_comprados.id_produtos = Produtos.Id WHERE Itens_comprados.id_compras = " + str(
        id_purchase)
    retorno = []

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        retorno.append({
            'item': x[0]
        })
    print(retorno)
    return retorno


def get_age_customer(id_customer):
    print("pegando idade cliente")
    sql_query = "SELECT data_nascimento FROM Clientes WHERE id = " + id_customer

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        print(x[0])
        return num_years(x[0])


def get_list_images():
    return list_images


def get_list_id_customes():
    return list_id_customers


def yearsago(years, from_date=None):
    if from_date is None:
        from_date = datetime.now()
    try:
        return from_date.replace(year=from_date.year - years)
    except:
        # Must be 2/29!
        assert from_date.month == 2 and from_date.day == 29  # can be removed
        return from_date.replace(month=2, day=28,
                                 year=from_date.year - years)


def num_years(begin, end=None):
    if end is None:
        end = datetime.now().date()
        print(end)
    num_years = int((end - begin).days / 365.25)
    if begin > yearsago(num_years, end):
        return num_years - 1
    else:
        return num_years
