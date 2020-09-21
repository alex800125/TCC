import csv
import json
import Database
import pandas as pd
from random import randint
from sklearn import preprocessing
from sklearn.metrics import confusion_matrix
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score
from sklearn.metrics import classification_report

sexo = ['M', 'F']


def criar_json_predict():
    retorno = []
    produtos = Database.get_produtos()

    for x in range(10000):
        cliente = []

        # esse numero depende do tamanho total da tabela de Produtos
        numero_aleatorio_produto = randint(0, 43)

        cliente.append({
            'sexo': sexo[randint(0, 1)],
            'idade': randint(18, 70),
            'produto': produtos[numero_aleatorio_produto].get('produto'),
            'valor': produtos[numero_aleatorio_produto].get('valor'),
            'comprou': randint(0, 1),
        })
        retorno.append(cliente)

    escrever_json(retorno)
    return retorno


def escrever_json(lista):
    with open('../dataset/teste1.json', 'w') as f:
        json.dump(lista, f)


def gerar_arvore_decisao(sexo, idade):
    clientes = pd.read_csv('../dataset/dataset.csv', sep=',', header=None)

    le = preprocessing.LabelEncoder()
    clientes = clientes.apply(le.fit_transform)

    X, Y, X_train, X_test, y_train, y_test = splitdataset(clientes)

    clf_gini = train_using_gini(X_train, X_test, y_train)
    clf_entropy = train_using_entropy(X_train, X_test, y_train)

    # Operational Phase

    # Prediction using gini
    print("X_test with clf_gini = ", X_test)
    y_pred_gini = prediction(X_test, clf_gini)
    cal_accuracy(y_test, y_pred_gini)

    # Prediction using entropy
    y_pred_entropy = prediction(X_test, clf_entropy)
    cal_accuracy(y_test, y_pred_entropy)

    # TODO criar tabela, teste_customer.csv é a temporaria com 6 itens, deve ter uma com todos os produtos

    create_single_test(sexo, idade)

    # create the test for the current customer
    current_customer = pd.read_csv('../dataset/teste_customer.csv', sep=',', header=None,
                                   skip_blank_lines=True, encoding='latin-1')
    le = preprocessing.LabelEncoder()
    current_customer = current_customer.apply(le.fit_transform)

    print("teste with clf_entropy")
    resposta_teste_entropy = prediction(current_customer, clf_entropy)
    print("teste with clf_gini")
    resposta_teste_gini = prediction(current_customer, clf_gini)

    response = criar_lista_recomendados(resposta_teste_entropy, resposta_teste_gini)

    return response


# Function to split the dataset
def splitdataset(balance_data):
    # Separating the target variable
    X = balance_data.values[:, 0:4]
    Y = balance_data.values[:, 4]

    # Splitting the dataset into train and test
    X_train, X_test, y_train, y_test = train_test_split(
        X, Y, test_size=0.1, random_state=100)

    return X, Y, X_train, X_test, y_train, y_test


# Function to perform training with giniIndex.
def train_using_gini(X_train, X_test, y_train):
    # Creating the classifier object
    clf_gini = DecisionTreeClassifier(criterion="gini",
                                      random_state=100, max_depth=3, min_samples_leaf=5)

    # Performing training
    clf_gini.fit(X_train, y_train)
    return clf_gini


# Function to perform training with entropy.
def train_using_entropy(X_train, X_test, y_train):
    # Decision tree with entropy
    clf_entropy = DecisionTreeClassifier(
        criterion="entropy", random_state=100,
        max_depth=3, min_samples_leaf=5)

    # Performing training
    clf_entropy.fit(X_train, y_train)
    return clf_entropy


# Function to make predictions
def prediction(X_test, clf_object):
    # Predicton on test with giniIndex
    y_pred = clf_object.predict(X_test)
    print("Predicted values:")
    print(y_pred)
    return y_pred


# Function to calculate accuracy
def cal_accuracy(y_test, y_pred):
    print("Confusion Matrix: ",
          confusion_matrix(y_test, y_pred))

    print("Accuracy : ",
          accuracy_score(y_test, y_pred) * 100)

    print("Report : ",
          classification_report(y_test, y_pred))


def create_single_test(sexo, idade):
    # teste = ["sexo", "idade", "produto", "valor", "comprou"]
    teste = []
    produtos = Database.get_produtos()

    for x in produtos:
        teste.append([str(sexo), str(idade), str(x.get('produto')), str(x.get('valor'))])

    print(teste)

    with open('../dataset/teste_customer.csv', mode='w') as employee_file:
        employee_writer = csv.writer(employee_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_ALL)

        for x in teste:
            employee_writer.writerow([x[0], x[1], x[2], x[3]])

    print("fim")


def criar_lista_recomendados(entropy, gini):
    resultado = []
    produtos = Database.get_produtos()

    for x in range(10):
        if entropy[x] == 1 and gini[x] == 1:
            resultado.append({"suggestion_item": produtos[x].get('produto')})

    return resultado
