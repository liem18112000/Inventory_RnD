import { Fragment, useEffect, useRef, useState } from "react";
import { Route } from "react-router";
import { Layout, Menu, Breadcrumb, Button } from 'antd';
import '../assets/styles/OverlayPanelDemo.css';
import { NotificationDialog } from "../notification/bell/NotificationDialog";
import { useKeycloak } from "@react-keycloak/web";
import '../assets/styles/Login.css';
import {menu} from "../config/PagesAndMenu";
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
                    <SiderMenu {...menu}/>
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background" style={{ padding: 0 }}>

                        <Button className="ant-btn" onClick={() => keycloak.logout()}>
                            {keycloak.tokenParsed.preferred_username} Logout
                        </Button>

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
                        <span> Copyright ©2022 Inventory System 1.0</span>
                    </Footer>
                </Layout>
            </Layout>
        </Fragment>
    }} />
}
export default AdminTemplate;
