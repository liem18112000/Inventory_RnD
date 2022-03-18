import React, { Component } from 'react';
import { Sidebar } from 'primereact/sidebar';
import { RecipeService } from '../../service/RecipeService';
import { sleep } from '../../core/utility/ComponentUtility';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';
import { ProgressBar } from 'primereact/progressbar';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Tag } from 'primereact/tag';

export class UploadImageForm extends Component {

    /**
     * Set default data state
     * @param props
     */
    constructor(props) {
        super(props);
        this.state = {
            data: {
                id: null,
                parentId: null,
                tenantId: '',
            },
            isMock: false,
            visible: false,
            totalSize: 0
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
        let totalSize = 0;
        e.files.forEach(file => {
            totalSize += (file.size || 0);
        });

        this.setState({
            totalSize
        }, () => {
            this.toast.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
        });
    }

    action = (id, parentId, isUpload = true) => {
        if (isUpload && id != null) {
            this.setUploadImage(id, parentId);
        }
    }

    setUploadImage(id, parentId) {
        this.recipeService.getByID(id, this.state.isMock).then(data => {
            this.setState({
                data: {
                    id: data ? data.id : null,
                    parentId: parentId,
                    tenantId: data ? data.tenantId : '',
                },
                id: data ? data.id : null,
                visible: true,
                totalSize: 0,
            })
        })
    }

    isSubmitValid = () => {
        return !(this.state.errors.name || this.state.errors.code || this.state.errors.recipeGroup);
    }

    /**
     * Handler all submit of save or update recipe form information
     * @param e
     */
    handleSubmit = (e) => {
        this.validateSubmit(() => {
            // If validation is valid, call API and get response
            if (this.isSubmitValid()) {
                let response = this.getResponseAfterSubmit();
                this.handleAfterSubmit(response);
            }
            // Otherwise, handle fail validation
            else {
                this.handleFailValidation();
            }
        })
    }

    /**
     * Handle fail validation
     */
    handleFailValidation() {
        this.toast.show({
            severity: 'error', summary: 'Upload Failed',
            content: 'Image is invalid',
            life: 1000
        });
    }

    /**
     * Handle event based on submit responses
     * @param response
     */
    handleAfterSubmit(response) {
        response
            .then(res => {
                if (!res) {
                    this.handleFailSubmit();
                } else {
                    this.handleSuccessSubmit();
                }
            })
    }

    /**
     * Handle success response
     */
    handleSuccessSubmit() {
        this.toast.show({
            severity: 'success', summary: 'Submit Success',
            detail: 'Upload Success',
            life: 1000
        });

        sleep(500).then(() => {
            this.props.refreshData();
            this.onHide();
        })
    }

    /**
     * Handle fail or error response
     */
    handleFailSubmit() {
        this.toast.show({
            severity: 'error', summary: 'Submit Fail',
            detail: 'Upload Failed',
            life: 1000
        });
    }

    /**
     * Retrieve response after submit form
     */
    getResponseAfterSubmit() {
        if (this.state.formHeader === this.state.editTitle) {
            console.log('Edit')
            return this.recipeService.updateRecipe(this.state.data, this.state.isMock);
        } else {
            console.log('Save')
            return this.recipeService.saveRecipe(this.state.data, this.state.isMock);
        }
    }

    /**
     * Call on form close
     */
    onHide = () => {
        this.setState({ visible: false, errors: {} });
    }

    onUpload() {
        this.toast.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
    }

    onTemplateSelect(e) {
        let totalSize = this.state.totalSize;
        e.files.forEach(file => {
            totalSize += file.size;
        });

        this.setState({
            totalSize
        });
    }

    onTemplateUpload(e) {
        let totalSize = 0;
        e.files.forEach(file => {
            totalSize += (file.size || 0);
        });

        this.setState({
            totalSize
        }, () => {
            this.toast.show({ severity: 'info', summary: 'Success', detail: 'File Uploaded' });
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
        const value = this.state.totalSize / 10000;
        const formatedValue = this.fileUploadRef ? this.fileUploadRef.formatSize(this.state.totalSize) : '0 B';

        return (
            <div className={className} style={{ backgroundColor: 'transparent', display: 'flex', alignItems: 'center' }}>
                {chooseButton}
                {uploadButton}
                {cancelButton}
                <ProgressBar value={value} displayValueTemplate={() => `${formatedValue} / 1 MB`} style={{ width: '300px', height: '20px', marginLeft: 'auto' }}></ProgressBar>
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
                <i className="pi pi-image p-mt-3 p-p-5" style={{ 'fontSize': '5em', borderRadius: '50%', backgroundColor: 'var(--surface-b)', color: 'var(--surface-d)' }}></i>
                <span style={{ 'fontSize': '1.2em', color: 'var(--text-color-secondary)' }} className="p-my-5">Drag and Drop Image Here</span>
            </div>
        )
    }

    /**
     * Render
     * @returns {JSX.Element}
     */
    render() {
        const chooseOptions = { icon: 'pi pi-fw pi-images', iconOnly: true, className: 'custom-choose-btn p-button-rounded p-button-outlined' };
        const uploadOptions = { icon: 'pi pi-fw pi-cloud-upload', iconOnly: true, className: 'custom-upload-btn p-button-success p-button-rounded p-button-outlined' };
        const cancelOptions = { icon: 'pi pi-fw pi-times', iconOnly: true, className: 'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined' };
        return (
            <Sidebar visible={this.state.visible} style={{ overflowY: "auto", width: "40em" }} position="right"
                blockScroll={true} baseZIndex={1000000} onHide={this.onHide} >
                <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
                <Tooltip target=".custom-upload-btn" content="Upload" position="bottom" />
                <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />
                <Toast ref={(el) => this.toast = el} />
                <h2>Upload Image</h2>
                <FileUpload ref={(el) => this.fileUploadRef = el} name="demo[]" url="https://primefaces.org/primereact/showcase/upload.php"
                    multiple accept="image/*" maxFileSize={1000000}
                    onUpload={this.onTemplateUpload} onSelect={this.onTemplateSelect}
                    onError={this.onTemplateClear} onClear={this.onTemplateClear}
                    headerTemplate={this.headerTemplate} itemTemplate={this.itemTemplate} emptyTemplate={this.emptyTemplate}
                    chooseOptions={chooseOptions} uploadOptions={uploadOptions} cancelOptions={cancelOptions} />
            </Sidebar>
        );
    }
}