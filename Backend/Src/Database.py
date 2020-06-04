import os
import face_recognition
import mysql.connector


def create_database_images():
    print("Criando banco de imagens")
    list_images = []
    folder = "../Known_Images/"

    for filename in os.listdir(folder):
        print(filename)
        img = face_recognition.load_image_file(os.path.join(folder, filename))
        if img is not None:
            face_encoding = face_recognition.face_encodings(img)[0]
            list_images.append(face_encoding)

    return list_images


def create_new_customer(name, birthday):
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
        cursor.close()
    except mysql.connector.Error as error:
        print("Failed to insert table {}".format(error))
        return {'status': False}
    finally:
        print("Success to insert a new customer!")
        return {'status': True}


def select_all_customer():
    print("Selecionando todos os clientes")
    list_names = []
    db = mysql.connector.connect(
        host="localhost",
        user="root",
        passwd="12345678",
        database="TESTE"
    )
    cursor = db.cursor()
    cursor.execute("SELECT name FROM Customer ")

    for x in cursor:
        list_names.append(x[0])

    cursor.close()
    print(list_names)
    return list_names
