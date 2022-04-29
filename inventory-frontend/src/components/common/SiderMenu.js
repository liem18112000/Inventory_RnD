import {Menu} from "antd";
import {NavLink} from "react-router-dom";

const { SubMenu, Item } = Menu;

const SiderMenu = (props) => {
  const {
    items,
    theme,
    mode
  } = props
  return (
      <Menu theme={theme} mode={mode}>
        {items.map((item, index) => {
          const {
            title,
            icon,
            path,
            children,
          } = item;
          const key = `sub-${index}`
          return (
              <SubMenu key={key} title={title} icon={icon}>
                {children.map((child, index) => {
                  const childPath = path + child.path;
                  const childKey = `${key}-child-${index}`;
                  const childIcon = child.icon;
                  const childTitle = child.title;
                  return (
                      <Item key={childKey} icon={childIcon}>
                        <NavLink key={`${childKey}-nav`} to={childPath}>{childTitle}</NavLink>
                      </Item>
                  )
                })}
              </SubMenu>
          )
        })}
      </Menu>
  )
}

export default SiderMenu;