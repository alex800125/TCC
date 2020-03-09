import face_recognition


def create_database_images():
    list_images = []

    known_image_path_1 = "../Known_Images/luan.jpg"
    known_image_path_2 = "../Known_Images/matheus.jpg"
    known_image_path_3 = "../Known_Images/alex.jpg"
    known_image_path_4 = "../Known_Images/leonardo.jpg"
    known_image_path_5 = "../Known_Images/Paulo.png"

    known_image_1 = face_recognition.load_image_file(known_image_path_1)
    face_encoding_1 = face_recognition.face_encodings(known_image_1)[0]
    list_images.append(face_encoding_1)

    known_image_2 = face_recognition.load_image_file(known_image_path_2)
    face_encoding_2 = face_recognition.face_encodings(known_image_2)[0]
    list_images.append(face_encoding_2)

    known_image_3 = face_recognition.load_image_file(known_image_path_3)
    face_encoding_3 = face_recognition.face_encodings(known_image_3)[0]
    list_images.append(face_encoding_3)

    known_image_4 = face_recognition.load_image_file(known_image_path_4)
    face_encoding_4 = face_recognition.face_encodings(known_image_4)[0]
    list_images.append(face_encoding_4)

    known_image_5 = face_recognition.load_image_file(known_image_path_5)
    face_encoding_5 = face_recognition.face_encodings(known_image_5)[0]
    list_images.append(face_encoding_5)

    return list_images


def create_database_names():
    list_names = [
        "Luan",
        "Matheus",
        "Alex",
        "Leonardo",
        "Paulo"
    ]

    return list_names