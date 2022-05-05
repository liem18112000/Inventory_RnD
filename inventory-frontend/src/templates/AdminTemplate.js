import { Fragment, useEffect, useRef, useState } from "react";
import { Route } from "react-router";
import { Layout, Breadcrumb, Button } from 'antd';
import {
    LogoutOutlined,
    UserOutlined
} from '@ant-design/icons';
import '../assets/styles/OverlayPanelDemo.css';
import { NotificationDialog } from "../notification/bell/NotificationDialog";
import { useKeycloak } from "@react-keycloak/web";
import '../assets/styles/Login.css';
import 'primeicons/primeicons.css';
import { menu } from "../config/PagesAndMenu";
import SiderMenu from "../components/common/SiderMenu";

const { Header, Content, Sider, Footer } = Layout;

const AdminTemplate = (props) => {

    const { keycloak } = useKeycloak();

    const { Component, ...restProps } = props;

    const [collapsed, setCollapsed] = useState(false);

    const onCollapse = collapsed => {
        setCollapsed(collapsed);
    };

    useEffect(() => {
        window.scrollTo(0, 0);

    })

    let bell = useRef(null)

    return <Route {...restProps} render={(propsRoute) => { //props.location, props.history, props.match
        return <Fragment>
            <Layout style={{ minHeight: '100vh' }}>
                <Sider
                    theme="dark"
                    mode="inline"
                    collapsible
                    breakpoint="sm"
                    collapsed={collapsed}
                    onCollapse={onCollapse}>
                    <div className="title">
                        <img src="https://res.cloudinary.com/ieltstinder/image/upload/v1648977417/Group_1_eco8m0.png"
                            alt={"Inventory"} />
                    </div>
                    <SiderMenu {...menu} />
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background" style={{ padding: 0 }}>
                        <div className="header_button">
                            <UserOutlined style={{ fontSize: '2rem', color: 'rgba(0, 0, 0, 0.6)', position: 'relative', bottom: '5px' }} />{keycloak.tokenParsed.preferred_username}
                            <Button onClick={() => keycloak.logout()} style={{ margin: '15px 15px 0px 20px', padding: 'unset', borderRadius: '20px' }}>
                                {/* <i className="pi pi-sign-out"></i> */}
                                <LogoutOutlined style={{ fontSize: '2rem', color: 'rgba(0, 0, 0, 0.6)', position: 'relative', bottom: '5px' }} />
                            </Button>
                        </div>
                        <NotificationDialog ref={bell} />

                    </Header>
                    <Content style={{ margin: '0 16px' }}>
                        <Breadcrumb style={{ margin: '16px 0' }}>
                        </Breadcrumb>
                        <div className="site-layout-background" style={{ padding: 24, minHeight: '85vh' }}>
                            <Component
                                bell={bell}
                                {...propsRoute} />
                        </div>
                    </Content>
                    <Footer className="footer" style={{ textAlign: 'center' }}>
                        <span> Copyright ©2022 Inventory System 1.1</span>
                    </Footer>
                </Layout>
            </Layout>
        </Fragment>
    }} />
}
export default AdminTemplate;
