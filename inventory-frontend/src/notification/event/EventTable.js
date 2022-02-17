import React, { Component } from 'react';
import { InputText } from 'primereact/inputtext';
import { NotificationService } from '../../service/NotificationService';
import { Dialog } from 'primereact/dialog';
import { InputTextarea } from 'primereact/inputtextarea';

export class EventTable extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: {
                id: null,
                message: ''
            },
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

    action = (id) => {
        this.setViewInformation(id);
    }

    setViewInformation(id) {
        this.notificationService.getEventById(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    message: data ? data.message : '',
                },
                id: data ? data.id : null,
                displayResponsive: true,
            })
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
        return (
            <Dialog header="Event Table" visible={this.state.displayResponsive} onHide={() => this.onHide('displayResponsive')} breakpoints={{ '960px': '75vw' }} style={{ width: '50vw' }}>
                <p>Datatable</p>
            </Dialog>
        );
    }
}

