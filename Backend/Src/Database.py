import base64
import os
import re

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


def keyFunc(afilename):
    nondigits = re.compile("\D")
    return int(nondigits.sub("", afilename))


def load_database_images():
    print("Criando banco de imagens")
    folder = "../Known_Images/"
    filenames = os.listdir(folder)

    for filename in sorted(filenames, key=keyFunc):
        print(filename)
        img = face_recognition.load_image_file(os.path.join(folder, filename))
        if img is not None:
            face_encoding = face_recognition.face_encodings(img)[0]
            list_images.append(face_encoding)


def create_new_customer(name, cpf, birthday, image):
    print("Criando novo cliente")
    db = connect_mysql()
    cursor = db.cursor()
    retorno = []

    try:
        cursor.execute("INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES (%s, %s, %s)", (name, cpf, birthday))
        db.commit()

        cursor.execute("SELECT MAX(id) FROM Clientes")
        count_max_id = cursor.fetchone()[0]
        print(count_max_id)

        create_new_customer_image(image, count_max_id)
        cursor.close()
        retorno.append({'status': 'true'})
    except mysql.connector.Error as error:
        print("Failed to insert table {}".format(error))
        retorno.append({'status': 'false'})

    return retorno


def update_customer(name, cpf, image):
    print("Atualizando cliente")
    db = connect_mysql()
    cursor = db.cursor()
    retorno = []

    try:
        cursor.execute("UPDATE Clientes SET nome = '" + name + "' WHERE cpf = " + cpf)

        db.commit()

        cursor.execute("SELECT id FROM Clientes WHERE cpf = " + cpf)
        id = cursor.fetchone()[0]

        create_new_customer_image(image, id)
        cursor.close()
        retorno.append({'status': 'true'})
    except mysql.connector.Error as error:
        print("Failed to insert table {}".format(error))
        retorno.append({'status': 'false'})

    return retorno


def load_database_customer_id():
    print("Selecionando todos os clientes")
    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute("SELECT id FROM Clientes")

    for x in cursor:
        list_id_customers.append(str(x[0]))
    cursor.close()


def create_new_customer_image(image, id):
    print("Criando nova imagem")

    file_path = "../Known_Images/" + str(id) + '.jpg'
    print(file_path)
    with open(file_path, 'wb') as f:
        f.write(image)

    # TODO verificar se um rosto foi reconhecida, em caso negativo, criar um jeito de retornar o erro
    # e o motivo onde essa funcao e chamada

    list_id_customers.append(id)
    img = face_recognition.load_image_file(file_path)
    new_face_encoding = face_recognition.face_encodings(img)[0]
    list_images.append(new_face_encoding)


def load_to_edit(cpf):
    print("Carregando dados para editar")
    folder = "../Known_Images/"
    retorno = []

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute("SELECT id, nome, data_nascimento FROM Clientes WHERE cpf = " + cpf)

    # 'image': face_recognition.load_image_file(os.path.join(folder, x[0] + ".jpg")),

    # print(cursor.)
    # if cursor.rowcount is 0:
    for x in cursor:
        retorno.append({
            'status': 'true',
            'name': x[1],
            'cpf': cpf,
            'birthday': str(x[2]),
            'image': str(load_image(x[0]))
        })
    cursor.close()
    # else:
    #     retorno.append({
    #         'status': 'false'
    #     })

    print(retorno)
    return retorno


def load_image(id):
    print(id)
    file_path = "../Known_Images/" + str(id) + ".jpg"
    print(file_path)
    with open(file_path, "rb") as img_file:
        image = base64.b64encode(img_file.read())

    return image


def search_customer(id_customer):
    print("procurando cliente")
    sql_query = "SELECT nome FROM Clientes WHERE id = " + str(id_customer)

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        return str(x[0])


def create_json(id_customer, name, image):
    print("criando JSON de retorno")
    sql_query = "SELECT MAX(data), valor_total, id FROM Compras WHERE id_cliente = " + id_customer
    retorno = []

    db = connect_mysql()
    cursor = db.cursor()
    cursor.execute(sql_query)

    for x in cursor:
        retorno.append({
            'name': name,
            'birthday': get_age_customer(id_customer),
            'ultima_compra_data': str(x[0]),
            'ultima_compra_valor': x[1],
            'itens_comprados': search_produtos(x[2]),
            'image': str(image),
        })
    return retorno


def search_produtos(id_purchase):
    print("procurando ultima compra")
    retorno = []

    if id_purchase is not None:
        sql_query = "SELECT Produtos.nome FROM Itens_comprados INNER JOIN Produtos ON Itens_comprados.id_produtos = Produtos.Id WHERE Itens_comprados.id_compras = " + str(
            id_purchase)

        db = connect_mysql()
        cursor = db.cursor()
        cursor.execute(sql_query)

        for x in cursor:
            retorno.append({
                'item': x[0]
            })
    else:
        retorno.append({
            'item': ''
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
        return num_years(x[0])


def get_list_images():
    return list_images


def get_list_id_customes():
    print(list_id_customers)
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
    num_years = int((end - begin).days / 365.25)
    if begin > yearsago(num_years, end):
        return num_years - 1
    else:
        return num_years
