import React, { useState } from 'react';

import {
    Col, Form,
    FormGroup, Label, Input,
    Button,
    FormFeedback 
} from 'reactstrap';

import AuthService from '../../../services/AuthService';

import Loader from '../../common/Loader';

function LoginForm({ onLogin = () => {} }) {

    const [dirty, setDirty] = useState({
        username: false,
        password: false,
    });

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [loading, setLoading] = useState(false);

    const login = async e => {
        e.preventDefault();

        const user = {
            username,
            password
        };

        setLoading(true);

        const responseData = await AuthService.login(user);

        setTimeout(() => {
            setLoading(false);
            onLogin(responseData);
        }, 1000);

        // TODO: close form

    }

    const onUsernameInputChange = e => {

        handleSetDirty('username');

        const input = e.target;

        const username = input.value;

        setUsername(username);

    }

    const onPasswordInputChange = e => {

        handleSetDirty('password');

        const input = e.target;

        const password = input.value;

        setPassword(password);

    }

    const handleSetDirty = (prop) => {

        if(!dirty[prop]) {
            const newDirty = {...dirty};
            newDirty[prop] = true;
            setDirty(newDirty);
        }

    }

    const isValidUsername = !!username;
    const isValidPassword = !!password && password.length > 5;

    const isFormValid = () => {
        return isValidUsername && isValidPassword;
    }

    return (

        <Form className="form position-relative p-2">
            {
                loading && <Loader />
            }
            <Col>
                <FormGroup>
                    <Label for="username">Usuario</Label>
                    <Input
                        valid={dirty.username && isValidUsername}
                        invalid={dirty.username && !isValidUsername}
                        type="text"
                        name="username"
                        id="username"
                        value={username}
                        onChange={onUsernameInputChange}
                    />
                    {
                        dirty.username && <FormFeedback>El nombre de usuario no puede estar vacío.</FormFeedback>
                    }
                </FormGroup>
            </Col>
            <Col>
                <FormGroup>
                    <Label for="password">Contraseña</Label>
                    <Input
                        valid={dirty.password && isValidPassword}
                        invalid={dirty.password && !isValidPassword}
                        type="password"
                        name="password"
                        id="examplePassword"
                        placeholder="********"
                        value={password}
                        onChange={onPasswordInputChange}
                    />
                    {
                        dirty.password && <FormFeedback>Introduce una contraseña de más de 5 caracteres.</FormFeedback>
                    }
                </FormGroup>
            </Col>

            <Button disabled={!isFormValid()} onClick={login}>Entrar</Button>
        </Form>

    );
}

export default LoginForm;