import React from 'react';

import { Table } from "reactstrap";

import { useSelector } from 'react-redux';

import './CategoryView.css';

import CategoryToolbar from './CategoryToolbar';

function CategoryView(props) {

    const categories = useSelector(state => state.categories);

    return (
        <div className="CategoryTable">
            <div className="float-right">
                <CategoryToolbar />
            </div>

            <Table bordered hover responsive>
                <thead>
                    <tr>
                        <th>Id</th>
                        <th>Clave</th>
                        <th>Categoría</th>
                    </tr>
                </thead>
                <tbody>
                {categories.map(({ id, key, category }) => (
                    <tr key={id}>
                        <td>{id}</td>
                        <td>{key}</td>
                        <td>{category}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </div>
    );

}



export default CategoryView;