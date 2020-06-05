import base64
from flask import Flask, request, jsonify
import datetime
import FaceValidation
import Database

app = Flask(__name__)


@app.route('/', methods=['GET'])
def confirmation():
    return "Server ON."


@app.route('/search', methods=['GET'])
def search_customer():
    response = FaceValidation.face_detection()
    return jsonify(response)


@app.route('/create', methods=['POST'])
def create_customer():
    data = request.get_json()

    image = base64.b64decode(data['image'])
    name = data['name']
    birthday = datetime.date(1999, 1, 1)

    response = Database.create_new_customer(name, birthday, image)

    return jsonify(response)


if __name__ == "__main__":
    app.run()
