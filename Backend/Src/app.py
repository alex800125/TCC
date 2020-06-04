from flask import Flask, jsonify
import datetime
import FaceValidation
import Database

app = Flask(__name__)


@app.route('/', methods=['GET'])
def confirmation():
    return "Server ON."


@app.route('/search', methods=['POST'])
def search_customer():
    response = FaceValidation.face_detection()
    return jsonify(response)


@app.route('/create', methods=['POST'])
def create_customer():
    birthday = datetime.date(1999, 1, 1)
    response = Database.create_new_customer("teste", birthday)
    return jsonify(response)


if __name__ == "__main__":
    app.run()
