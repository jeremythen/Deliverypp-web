import React from 'react';

import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

function DModal(props) {
    return (
        <div>
        <Modal isOpen={props.showModal}>
            <ModalHeader toggle={props.toggle}>{props.title}</ModalHeader>
            <ModalBody>
                {
                    props.children
                }
            </ModalBody>
            <ModalFooter>
            <Button color="success" onClick={props.onSave}>Guardar</Button>{' '}
            <Button color="secondary" onClick={props.onCancel}>Cancelar</Button>
            </ModalFooter>
        </Modal>
        </div>
    )
}

export default DModal;