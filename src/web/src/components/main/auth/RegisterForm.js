import React, { useState } from 'react';

import axios from 'axios';

import {
    Col, Form,
    FormGroup, Label, Input,
    Button, FormText, FormFeedback 
} from 'reactstrap';

import validator from 'validator';

import AuthService from '../../../services/AuthService';

import Loader from '../../common/Loader';

function RegisterForm({onRegister = () => {}}) {

    const [dirty, setDirty] = useState({
        username: false,
        email: false,
        password: false,
        name: false,
        telephone: false 
    });

    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [lastName, setLastName] = useState('');
    const [telephone, setTelephone] = useState('');

    const [loading, setLoading] = useState(false);

    const register = async e => {
        e.preventDefault();

        const user = {
            name,
            username,
            email,
            password,
            lastName,
            telephone
        }

        setLoading(true);

        const response = await AuthService.register(user);

        if(response && response.token) {
            localStorage.setItem('deliverypp_user_login_token', response.token);
        }

        clearForm();

        setTimeout(() => {
            setLoading(false);
            onRegister(response);
        }, 1000);

    }

    const clearForm = () => {

        setUsername('');
        setEmail('');
        setPassword('');
        setName('');
        setLastName('');
        setTelephone('');

        setDirty({
            username: false,
            email: false,
            password: false,
            name: false,
            telephone: false 
        });

    }

    const onUsernameInputChange = e => {

        handleSetDirty('username');

        const input = e.target;

        const username = input.value;

        setUsername(username);

    }

    const onEmailInputChange = e => {

        handleSetDirty('email');

        const input = e.target;

        const email = input.value;

        setEmail(email);

    }

    const onPasswordInputChange = e => {

        handleSetDirty('password');

        const input = e.target;

        const password = input.value;

        setPassword(password);

    }

    const onNameInputChange = e => {

        handleSetDirty('name');

        const input = e.target;

        const name = input.value;

        setName(name);
    }

    const onLastNameInputChange = e => {
        const input = e.target;

        const lastName = input.value;

        setLastName(lastName);
    }

    const onTelephoneInputChange = e => {

        handleSetDirty('telephone');

        const input = e.target;

        const telephone = input.value;

        setTelephone(telephone);
    }

    const handleSetDirty = (prop) => {

        if(!dirty[prop]) {
            const newDirty = {...dirty};
            newDirty[prop] = true;
            setDirty(newDirty);
        }

    }

    const isValidEmail = !!email && validator.isEmail(email);
    const isValidUsername = !!username;
    const isValidName = !!name;
    const isValidPassword = !!password && password.length > 5;
    const isValidTelephone = !!telephone && telephone.length === 10;

    const isFormValid = () => {
        return isValidEmail && isValidUsername && isValidName && isValidPassword && isValidTelephone;
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
                        required
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
                    <Label for="email">Email</Label>
                    <Input
                        valid={dirty.email && isValidEmail}
                        invalid={dirty.email && !isValidEmail}
                        type="email"
                        name="email"
                        id="exampleEmail"
                        placeholder="youremail@email.com"
                        required
                        value={email}
                        onChange={onEmailInputChange}
                    />
                    {
                        dirty.email && <FormFeedback>Introduce un email válido.</FormFeedback>
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
                        id="password"
                        placeholder="********"
                        value={password}
                        onChange={onPasswordInputChange}
                        required
                    />
                    {
                        dirty.password && <FormFeedback>Introduce una contraseña de más de 5 caracteres.</FormFeedback>
                    }
                </FormGroup>
            </Col>

            <Col>
                <FormGroup>
                    <Label for="name">Nombre</Label>
                    <Input
                        valid={dirty.name && isValidName}
                        invalid={dirty.name && !isValidName}
                        type="text"
                        name="name"
                        id="name"
                        value={name}
                        onChange={onNameInputChange}
                        required
                    />
                    {
                        dirty.name && <FormFeedback>El nombre no puede estar vacío.</FormFeedback>
                    }
                </FormGroup>
            </Col>

            <Col>
                <FormGroup>
                    <Label for="lastname">Apellido</Label>
                    <Input
                        type="text"
                        name="lastname"
                        id="lastname"
                        value={lastName}
                        onChange={onLastNameInputChange}
                        required
                    />
                </FormGroup>
            </Col>

            <Col>
                <FormGroup>
                    <Label for="telephone">Teléfono</Label>
                    <Input
                        valid={dirty.telephone && isValidTelephone}
                        invalid={dirty.telephone && !isValidTelephone}
                        type="number"
                        name="telephone"
                        id="telephone"
                        value={telephone}
                        onChange={onTelephoneInputChange}
                        placeholder="8095005000"
                        required
                    />
                    {
                        dirty.name && <FormFeedback>Introduce un teléfono válido.</FormFeedback>
                    }
                </FormGroup>
            </Col>

            <Button disabled={!isFormValid()} onClick={register}>Registrarse</Button>
        </Form>

    );
}

export default RegisterForm;