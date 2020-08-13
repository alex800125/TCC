import Database as Db
import face_recognition
import cv2
import numpy as np
import base64
import hashlib

Db.load_database_customer_id()
Db.load_database_images()


def face_detection():
    video_capture = cv2.VideoCapture(0)

    process_this_frame = True
    retorno = []
    find_face = False

    while True:
        ret, image = video_capture.read()

        print("tratando imagem")
        # Resize image to 1/4 size for faster face recognition processing
        small_image = cv2.resize(image, (0, 0), fx=0.25, fy=0.25)

        # Convert the image from BGR color (which OpenCV uses) to RGB color (which face_recognition uses)
        unknown_image = small_image[:, :, ::-1]

        # Only process every other frame of video to save time
        if process_this_frame:
            print("reconhecendo imagem")
            face_locations = face_recognition.face_locations(unknown_image)
            face_encodings = face_recognition.face_encodings(unknown_image, face_locations)

            print("atribuindo o nome a cada face reconhecida")
            face_id = []
            for face_encoding in face_encodings:
                # See if the face is a match for the known face(s)
                matches = face_recognition.compare_faces(Db.get_list_images(), face_encoding)

                # Use the known face with the smallest distance to the new face
                face_distances = face_recognition.face_distance(Db.get_list_images(), face_encoding)
                best_match_index = np.argmin(face_distances)
                if matches[best_match_index]:
                    id = Db.get_list_id_customes()[best_match_index]
                    face_id.append(id)

        process_this_frame = not process_this_frame

        print("criando imagem com marcações")
        # Display the results
        for (top, right, bottom, left), id in zip(face_locations, face_id):
            name = Db.search_customer(id)
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

            print("armazenando imagem")
            if name:
                file_path = "../Recognized_Image/" + name + ".jpg"
                cv2.imwrite(file_path, image)

                # with open(file_path, "rb") as img_file:
                #     imageBase64 = base64.b64encode(img_file.read())
                #     imageSha256 = hashlib.sha256(imageBase64.rstrip()).hexdigest()
                #     hashlib.sha256(imageBase64.encode()).hexdigest()

                retorno.append({
                    'name': name,
                    'age': Db.get_age_customer(id),
                    'ultima_compra': Db.search_last_purchase(id)
                })
                find_face = True

        if find_face:
            break

    # Release handle to the webcam
    video_capture.release()
    cv2.destroyAllWindows()

    # Debug
    print(retorno)
    return retorno
