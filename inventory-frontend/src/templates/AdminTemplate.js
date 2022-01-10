import { Fragment, useEffect, useState } from "react";
import { Route } from "react-router";
import {Layout, Menu, Breadcrumb, Button} from 'antd';
import { NavLink } from "react-router-dom";
// import { history } from "../App.js";
import "./AdminTemplate.css"
import {
    DesktopOutlined,
    TeamOutlined,
    OneToOneOutlined,
    ShoppingCartOutlined,
    BellOutlined
} from '@ant-design/icons';
import {logout} from "../core/security/AuthenticateService";

const { Header, Content, Sider } = Layout;
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

    return <Route {...restProps} render={(propsRoute) => { //props.location, props.history, props.match
        return <Fragment>
            <Layout style={{ minHeight: '100vh' }}>
                <Sider theme="light" collapsible collapsed={collapsed} onCollapse={onCollapse}>
                    <div className="title">
                        <h1>Inventory System</h1>
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
                                <NavLink to="/suggest-taxon">Suggest Taxon</NavLink>
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
                        <Menu.Item key="7" icon={<BellOutlined />}>
                            <NavLink to="/notification">Notification</NavLink>
                        </Menu.Item>
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background" style={{ padding: 0 }} >

                        {/*TODO: Style logout button*/}
                        <Button onClick={(e) => logout()}>Logout</Button>
                    </Header>
                    <Content style={{ margin: '0 16px' }}>
                        <Breadcrumb style={{ margin: '16px 0' }}>
                        </Breadcrumb>
                        <div className="site-layout-background" style={{ padding: 24, minHeight: '85vh' }}>
                            <Component {...propsRoute} />
                        </div>
                    </Content>
                </Layout>
            </Layout>
        </Fragment>
    }} />
}
export default AdminTemplate;
