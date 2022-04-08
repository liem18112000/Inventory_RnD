import { Fragment, useEffect, useRef, useState } from "react";
import { Route } from "react-router";
import { Layout, Menu, Breadcrumb, Button } from 'antd';
import { NavLink } from "react-router-dom";
// import { history } from "../App.js";
import {
    DesktopOutlined,
    TeamOutlined,
    OneToOneOutlined,
    ShoppingCartOutlined,
    BellOutlined
} from '@ant-design/icons';
import { logout } from "../core/security/AuthenticateService";
import '../assets/styles/OverlayPanelDemo.css';
import { NotificationDialog } from "../notification/bell/NotificationDialog";
// import { Footer } from "antd/lib/layout/layout";

const { Header, Content, Sider, Footer } = Layout;
const { SubMenu } = Menu;



const AdminTemplate = (props) => { //path, exact, Component
    // console.log(props);

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
                    theme="light"
                    collapsible
                    breakpoint="sm"
                    collapsed={collapsed}
                    onCollapse={onCollapse}>
                    <div className="title">
                        {/* <h1>Inventory System</h1> */}
                        <img
                            src="https://res.cloudinary.com/ieltstinder/image/upload/v1648977417/Group_1_eco8m0.png" alt=''
                        />
                    </div>
                    <Menu
                        // defaultSelectedKeys={['2']} 
                        mode="inline">
                        <SubMenu key="sub1" title="Dashboard" icon={<DesktopOutlined />}>
                            <Menu.Item key="1" >
                                <NavLink to="/ingredient-inventory">Ingredient Inventory</NavLink>
                            </Menu.Item>
                        </SubMenu>
                        <SubMenu key="sub2" title="Ingredient Mgt" icon={<ShoppingCartOutlined />}>
                            <Menu.Item key="2" >
                                <NavLink to="/ingredient">Ingredient Categories</NavLink>
                            </Menu.Item>
                            <Menu.Item key="3" >
                                <NavLink to="/taxon">Suggest Taxon</NavLink>
                            </Menu.Item>
                        </SubMenu>
                        <SubMenu key="sub3" title="Recipe Mgt" icon={<OneToOneOutlined />}>
                            <Menu.Item key="4" >
                                <NavLink to="/recipe">Recipe Groups</NavLink>
                            </Menu.Item>
                            <Menu.Item key="5" >
                                <NavLink to="/recipes">Recipes</NavLink>
                            </Menu.Item>
                        </SubMenu>
                        <SubMenu key="sub4" title="Supplier Mgt" icon={<TeamOutlined />} >
                            <Menu.Item key="6" >
                                <NavLink to="/supplier">Supplier Group</NavLink>
                            </Menu.Item>
                        </SubMenu>
                        <SubMenu key="sub5" title="Notification Mgt" icon={<BellOutlined />}>
                            <Menu.Item key="7" >
                                <NavLink to="/notification/event">Event</NavLink>
                            </Menu.Item>
                        </SubMenu>
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background" style={{ padding: 0 }}>

                        {/*TODO: Style logout button*/}
                        <Button onClick={(e) => logout()}>Logout</Button>

                        <NotificationDialog ref={bell} />

                    </Header>
                    <Content style={{ margin: '0 16px' }}>
                        <Breadcrumb style={{ margin: '16px 0' }}>
                        </Breadcrumb>
                        <div className="site-layout-background" style={{ padding: 24, minHeight: '85vh' }}>
                            <Component
                                // bell={bell}
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
