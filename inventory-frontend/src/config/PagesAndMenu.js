import {
  AppstoreOutlined, BarChartOutlined,
  BellOutlined, ClusterOutlined, DashboardOutlined,
  MailOutlined,
  OneToOneOutlined, ShopOutlined,
  ShoppingCartOutlined,
  TableOutlined,
  TeamOutlined
} from "@ant-design/icons";

const sider = {
  theme: "dark",
  breakpoint: "sm"
}

const menu = {
  theme: "dark",
  mode: "inline",
  items: [
    {
      title: "Dashboard",
      icon: <DashboardOutlined />,
      path: "",
      children: [
        {
          path: "/ingredient-inventory",
          title: "Dashboard",
          icon: <BarChartOutlined />
        }
      ]
    },
    {
      title: "Ingredient Mgt",
      icon: <ShoppingCartOutlined />,
      path: "",
      children: [
        {
          path: "/ingredient",
          title: "Categories",
          icon: <ClusterOutlined />
        },
        {
          path: "/taxon",
          title: "Suggest Taxon",
          icon: <AppstoreOutlined />
        }
      ]
    },
    {
      title: "Recipe Mgt",
      icon: <OneToOneOutlined />,
      path: "",
      children: [
        {
          path: "/recipe",
          title: "Groups",
          icon: <TableOutlined />
        },
        {
          path: "/recipes",
          title: "Recipes",
          icon: <TableOutlined />
        }
      ]
    },
    {
      title: "Supplier Mgt",
      icon: <TeamOutlined />,
      path: "",
      children: [
        {
          path: "/supplier",
          title: "Group",
          icon: <ShopOutlined />
        }
      ]
    },
    {
      title: "Notification Mgt",
      icon: <BellOutlined />,
      path: "/notification",
      children: [
        {
          path: "/event",
          title: "Event",
          icon: <MailOutlined />
        }
      ]
    }
  ]
}

export {
    menu,
    sider
}