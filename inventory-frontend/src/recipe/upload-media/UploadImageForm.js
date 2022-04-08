import React, { Component } from 'react';
import { Sidebar } from 'primereact/sidebar';
import { RecipeService } from '../../service/RecipeService';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';
import { ProgressBar } from 'primereact/progressbar';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Tag } from 'primereact/tag';
import { baseRecipeAPI } from "../../constant";
import { sleep } from "../../core/utility/ComponentUtility";

export class UploadImageForm extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            id: null,
            isMock: false,
            visible: false,
            totalSize: 0,
        }
        this.recipeService = new RecipeService();
        this.onTemplateUpload = this.onTemplateUpload.bind(this)
        this.onTemplateSelect = this.onTemplateSelect.bind(this);
        this.onTemplateClear = this.onTemplateClear.bind(this);
        this.headerTemplate = this.headerTemplate.bind(this);
        this.itemTemplate = this.itemTemplate.bind(this);
        this.emptyTemplate = this.emptyTemplate.bind(this);
    }

    /**
     * Function is called after component is required
     */
    componentDidMount() {
    }

    onTemplateUpload(e) {
        if ([...e.files].length > 1) {
            this.toast.show({ severity: 'error', summary: 'Upload failed', detail: 'Only one image is allowed' });
            return;
        }

        let totalSize = 0;
        [...e.files].forEach(file => {
            totalSize += (file.size || 0);
        });

        this.setState({
            ...this.state,
            "totalSize": totalSize
        }, () => {
            this.toast.show({ severity: 'success', summary: 'Success', detail: 'File Uploaded' });
            sleep().then(this.onHide)
        });
    }

    action = (id) => {
        this.setState({
            visible: true,
            id: id
        })
    }

    /**
     * Call on form close
     */
    onHide = () => {
        this.setState({ visible: false, errors: {} });
    }

    onTemplateSelect(e) {
        let totalSize = this.state.totalSize;
        [...e.files].forEach(file => {
            totalSize += file.size;
        });

        this.setState({
            ...this.state,
            "totalSize": totalSize
        });
    }

    onTemplateRemove(file, callback) {
        this.setState((prevState) => ({
            totalSize: prevState.totalSize - file.size
        }), callback);
    }

    onTemplateClear() {
        this.setState({ totalSize: 0 });
    }

    headerTemplate(options) {
        const { className, chooseButton, uploadButton, cancelButton } = options;
        const value = this.state.totalSize / 80000;
        const formatedValue = this.fileUploadRef ? this.fileUploadRef.formatSize(this.state.totalSize) : '0 B';

        return (
            <div className={className} style={{ backgroundColor: 'transparent', display: 'flex', alignItems: 'center' }}>
                {chooseButton}
                {uploadButton}
                {cancelButton}
                <ProgressBar value={value} displayValueTemplate={() => `${formatedValue} / 8 MB`} style={{
                    width: '300px',
                    height: '20px',
                    marginLeft: 'auto'
                }} />
            </div>
        );
    }

    itemTemplate(file, props) {
        return (
            <div className="p-d-flex p-ai-center p-flex-wrap">
                <div className="p-d-flex p-ai-center" style={{ width: '40%' }}>
                    <img alt={file.name} role="presentation" src={file.objectURL} width={100} />
                    <span className="p-d-flex p-dir-col p-text-left p-ml-3">
                        {file.name}
                        <small>{new Date().toLocaleDateString()}</small>
                    </span>
                </div>
                <Tag value={props.formatSize} severity="warning" className="p-px-3 p-py-2" />
                <Button type="button" icon="pi pi-times" className="p-button-outlined p-button-rounded p-button-danger p-ml-auto" onClick={() => this.onTemplateRemove(file, props.onRemove)} />
            </div>
        )
    }

    emptyTemplate() {
        return (
            <div className="p-d-flex p-ai-center p-dir-col">
                <i className="pi pi-image p-mt-3 p-p-5" style={{
                    'fontSize': '5em',
                    borderRadius: '50%',
                    backgroundColor: 'var(--surface-b)',
                    color: 'var(--surface-d)'
                }} />
                <span style={{ 'fontSize': '1.2em', color: 'var(--text-color-secondary)' }} className="p-my-5">
                    Drag and Drop Image Here
                </span>
            </div>
        )
    }

    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        const chooseOptions = {
            icon: 'pi pi-fw pi-images',
            iconOnly: true,
            className: 'custom-choose-btn p-button-rounded p-button-outlined'
        };
        const uploadOptions = {
            icon: 'pi pi-fw pi-cloud-upload',
            iconOnly: true,
            className: 'custom-upload-btn p-button-success p-button-rounded p-button-outlined'
        };
        const cancelOptions = {
            icon: 'pi pi-fw pi-times',
            iconOnly: true,
            className: 'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined'
        };
        return (
            <Sidebar
                visible={this.state.visible}
                style={{ overflowY: "auto", width: "40em" }}
                position="right"
                blockScroll={true}
                baseZIndex={1000000}
                onHide={this.onHide} >
                <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
                <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
                <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />
                <Toast ref={(el) => this.toast = el} />
                <h2>Upload Image</h2>
                <FileUpload
                    ref={(el) => this.fileUploadRef = el}
                    name="image"
                    url={baseRecipeAPI() + "/child/" + this.state.id + "/image"}
                    accept="image/*"
                    multiple
                    maxFileSize={80000000}
                    onUpload={this.onTemplateUpload}
                    onSelect={this.onTemplateSelect}
                    onError={this.onTemplateClear}
                    onClear={this.onTemplateClear}
                    headerTemplate={this.headerTemplate}
                    itemTemplate={this.itemTemplate}
                    emptyTemplate={this.emptyTemplate}
                    chooseOptions={chooseOptions}
                    uploadOptions={uploadOptions}
                    cancelOptions={cancelOptions} />
            </Sidebar>
        );
    }
}