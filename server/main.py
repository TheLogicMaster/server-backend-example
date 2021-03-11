from flask import Flask, request
import json
from os import path
from flask_httpauth import HTTPBasicAuth
from werkzeug.security import generate_password_hash, check_password_hash

app = Flask(__name__)
auth = HTTPBasicAuth()

data = {
    'messages': [
        {
            'user': 'Server',
            'message': 'Hello!'
        }
    ],
    'users': {
        'user': generate_password_hash('1234')
    }
}


def load_data():
    if not path.exists('data.json'):
        return
    with open('data.json') as f:
        global data
        data = json.load(f)


def save_data():
    with open('data.json', 'w') as f:
        json.dump(data, f)


@auth.verify_password
def verify_password(username, password):
    if username in data['users'] and \
            check_password_hash(data['users'][username], password):
        return username


@app.route('/signup')
def signup():
    password = request.args.get('password', default='')
    username = request.args.get('username', default='')
    if username in data['users']:
        return 'Sorry, that username is already taken'
    for user in data['users']:
        if check_password_hash(data['users'][user], password):
            return f'Sorry, that password is already taken by {user}'
    data['users'][username] = generate_password_hash(password)
    save_data()
    print(f'{username} signed up using password: {password}')
    return 'Successfully created account!'


@app.route('/login')
@auth.login_required
def login():
    print(f'{auth.current_user()} signed in')
    return f'Welcome, {auth.current_user()}!'


@app.route('/post', methods=['post'])
@auth.login_required
def post():
    message = request.get_data(as_text=True)
    data['messages'].append({
        'user': auth.current_user(),
        'message': message
    })
    save_data()
    print(f'{auth.current_user()} posted message: {message}')
    return 'Message posted'


@app.route('/messages')
@auth.login_required
def messages():
    return {'messages': data['messages']}


if __name__ == '__main__':
    load_data()
    app.run('0.0.0.0', '6969')
