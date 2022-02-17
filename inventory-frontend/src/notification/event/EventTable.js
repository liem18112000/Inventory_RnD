import React, { Component } from 'react';
import { NotificationService } from '../../service/NotificationService';
import { Dialog } from 'primereact/dialog';
import Column from 'antd/lib/table/Column';
import { DataTable } from 'primereact/datatable';

export class EventTable extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            type: "",
            isMock: false,
            errors: {},
            displayResponsive: false,
            position: 'center'
        }
        this.notificationService = new NotificationService();
        this.onClick = this.onClick.bind(this);
        this.onHide = this.onHide.bind(this);
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
    }

    action = (data, type) => {
        const json = JSON.parse(data)
        this.setViewInformation(json, type);
        console.log(json)
    }

    setViewInformation(data, type) {
            this.setState({
                data: data,
                type: type,
                displayResponsive: true,
            })
        } 

    onClick(name, position) {
        let state = {
            [`${name}`]: true
        };

        if (position) {
            state = {
                ...state,
                position
            }
        }

        this.setState(state);
    }

    onHide(name) {
        this.setState({
            [`${name}`]: false
        });
    }

    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        if (this.state.type === "Ingredient item monthly statistics")
        {
            return (
                <Dialog header="Event Table" visible={this.state.displayResponsive} onHide={() => this.onHide('displayResponsive')} breakpoints={{ '960px': '75vw' }} style={{ width: '70vw'}}>
                    <div className="card">
                        <DataTable value={this.state.data}>
                            <Column field="ingredientCode" header="Code"></Column>
                            <Column field="ingredientName" header="Name"></Column>
                            <Column field="quantity" header="Quantity"></Column>
                            <Column field="unitType" header="Unit Type"></Column>
                            <Column field="unit" header="Unit"></Column>
                        </DataTable>
                    </div>
                </Dialog>
            );
        }
         else if (this.state.type === "Ingredient item quantity is low"){
            return (
                <Dialog header="Event Table" visible={this.state.displayResponsive} onHide={() => this.onHide('displayResponsive')} breakpoints={{ '960px': '75vw' }} style={{ width: '70vw'}}>
                    <div className="card">
                        <DataTable value={this.state.data}>
                            <Column field="ingredientCode" header="Code"></Column>
                            <Column field="ingredientName" header="Name"></Column>
                            <Column field="minQuantity" header="Minimum Quantity"></Column>
                            <Column field="currentQuantity" header="Current Quantity"></Column>
                        </DataTable>
                    </div>
                </Dialog>
            );
        } 
        return null;
    }
}

