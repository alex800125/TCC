import Database as Db
import face_recognition
import cv2
import numpy as np


def face_detection():
    known_face_encodings = Db.create_database_images()
    known_face_names = Db.create_database_names()

    unknown_image_path = "../Images/imageToAnalise.jpg"

    print("tratando imagem")
    image = cv2.imread(unknown_image_path)

    # Resize image to 1/4 size for faster face recognition processing
    unknown_image = cv2.resize(image, (0, 0), fx=0.25, fy=0.25)

    # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
    # unknown_image = small_frame[:, :, ::-1]

    print("reconhecendo imagem")
    face_locations = face_recognition.face_locations(unknown_image)
    face_encodings = face_recognition.face_encodings(unknown_image, face_locations)

    print("atribuindo o nome a cada face reconhecida")
    face_names = []
    for face_encoding in face_encodings:
        # See if the face is a match for the known face(s)
        matches = face_recognition.compare_faces(known_face_encodings, face_encoding)
        name = "Unknown"

        # # If a match was found in known_face_encodings, just use the first one.
        # if True in matches:
        #     first_match_index = matches.index(True)
        #     name = known_face_names[first_match_index]

        # Or instead, use the known face with the smallest distance to the new face
        face_distances = face_recognition.face_distance(known_face_encodings, face_encoding)
        best_match_index = np.argmin(face_distances)
        if matches[best_match_index]:
            name = known_face_names[best_match_index]

        face_names.append(name)

    print("criando imagem com marcações")
    # Display the results
    for (top, right, bottom, left), name in zip(face_locations, face_names):
        # Scale back up face locations since the frame we detected in was scaled to 1/4 size
        top *= 4
        right *= 4
        bottom *= 4
        left *= 4

        # Draw a box around the face
        cv2.rectangle(image, (left, top), (right, bottom), (0, 0, 255), 2)

        # Draw a label with a name below the face
        cv2.rectangle(image, (left, bottom - 35), (right, bottom), (0, 0, 255), cv2.FILLED)
        font = cv2.FONT_HERSHEY_DUPLEX
        cv2.putText(image, name, (left + 6, bottom - 6), font, 1.0, (255, 255, 255), 1)

    print("salvando a imagem")
    # Saving the image generated
    cv2.imwrite("../Recognized_Image/ImageRecognized.jpg", image)
