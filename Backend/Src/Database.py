import os
import face_recognition
import mysql.connector

list_images = []
list_names = []


def create_database_images():
    print("Criando banco de imagens")
    folder = "../Known_Images/"
    for filename in os.listdir(folder):
        print(filename)
        img = face_recognition.load_image_file(os.path.join(folder, filename))
        if img is not None:
            face_encoding = face_recognition.face_encodings(img)[0]
            list_images.append(face_encoding)


def create_database_names():
    print("Selecionando todos os clientes")
    db = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="12345678",
        database="TESTE"
    )
    cursor = db.cursor()
    cursor.execute("SELECT name FROM Customer")

    for x in cursor:
        list_names.append(x[0])
    cursor.close()


def create_new_customer(name, birthday, image):
    print("Criando novo cliente")
    db = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="12345678",
        database="TESTE"
    )
    cursor = db.cursor()

    try:
        cursor.execute("INSERT INTO Customer (name, birthday) VALUES (%s, %s)", (name, birthday))
        db.commit()

        cursor.execute("SELECT MAX(id) FROM Customer")
        count_max_id = cursor.fetchone()[0]
        print(count_max_id)

        create_new_customer_image(image, count_max_id)
        list_names.append(name)
        cursor.close()
    except mysql.connector.Error as error:
        print("Failed to insert table {}".format(error))
        return {'status': False}
    finally:
        print("Success to insert a new customer!")
        return {'status': True}


def create_new_customer_image(image, count_max_id):
    print("Criando nova imagem")

    file_path = "../Known_Images/" + str(count_max_id) + '.jpg'
    print(file_path)
    with open(file_path, 'wb') as f:
        f.write(image)

    img = face_recognition.load_image_file(file_path)
    new_face_encoding = face_recognition.face_encodings(img)[0]
    list_images.append(new_face_encoding)


def get_list_images():
    return list_images


def get_list_names():
    return list_names
