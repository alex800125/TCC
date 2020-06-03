from flask import Flask, jsonify
import FaceValidation

app = Flask(__name__)


@app.route('/', methods=['GET'])
def confirmation():
    return "Servidor Online."


@app.route('/', methods=['POST'])
def verification():
    response = FaceValidation.face_detection()
    return jsonify(response)


if __name__ == "__main__":
    app.run()
