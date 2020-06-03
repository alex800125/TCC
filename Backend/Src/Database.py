import os
import face_recognition


def create_database_images():
    print("Criando banco")
    list_images = []
    folder = "../Known_Images/"

    for filename in os.listdir(folder):
        img = face_recognition.load_image_file(os.path.join(folder, filename))
        if img is not None:
            face_encoding = face_recognition.face_encodings(img)[0]
            list_images.append(face_encoding)

    return list_images


def create_database_names():
    list_names = [
        "Leonardo",
        "Luan",
        "Matheus",
        "Paulo",
        "Alex"
    ]

    return list_names
