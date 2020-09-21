import base64
from flask import Flask, request, jsonify
import FaceValidation
import Database
import Predict

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
    cpf = data['cpf']
    birthday = str(data['birthday'])
    sexo = data['sexo']
    response = Database.create_new_customer(name, cpf, birthday, sexo, image)

    return jsonify(response)


@app.route('/edit_check', methods=['POST'])
def edit_check_customer():
    data = request.get_json()

    cpf = data['cpf']
    response = Database.load_to_edit(cpf)
    return jsonify(response)


@app.route('/edit', methods=['POST'])
def edit_customer():
    data = request.get_json()

    image = base64.b64decode(data['image'])
    name = data['name']
    cpf = data['cpf']
    response = Database.update_customer(name, cpf, image)
    return jsonify(response)


@app.route('/predict', methods=['GET'])
def predict_create_training():
    response = Predict.criar_json_predict()
    return jsonify(response)


@app.route('/arvore_decisao', methods=['GET'])
def predict_test():
    response = Predict.gerar_arvore_decisao()
    return jsonify(response)


if __name__ == "__main__":
    app.run()
