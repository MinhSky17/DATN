import React, { useState } from 'react';
import { Layout, Menu } from 'antd';
import {
  UserOutlined,
  ShoppingOutlined,
  AppstoreOutlined,
  ShoppingCartOutlined,
  DashboardOutlined,
  LogoutOutlined
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import './DashboardAdmin.css';

const { Header, Sider, Content } = Layout;

const DashboardAdmin = ({ children }) => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();

  const menuItems = [
    {
      key: '/admin',
      icon: <DashboardOutlined />,
      label: 'Dashboard'
    },
    {
      key: '/admin/account-management',
      icon: <UserOutlined />,
      label: 'Quản lý tài khoản'
    },
    {
      key: '/admin/category-management',
      icon: <AppstoreOutlined />,
      label: 'Quản lý danh mục'
    },
    {
      key: '/admin/product-management',
      icon: <ShoppingOutlined />,
      label: 'Quản lý sản phẩm'
    },
    {
      key: '/admin/order-management',
      icon: <ShoppingCartOutlined />,
      label: 'Quản lý đơn hàng'
    }
  ];

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <div className="admin-logo">
          <h2>{collapsed ? 'CS' : 'COSHINE'}</h2>
        </div>
        <Menu
          theme="dark"
          mode="inline"
          defaultSelectedKeys={[window.location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
        />
        <div className="logout-button">
          <Menu
            theme="dark"
            mode="inline"
            items={[
              {
                key: 'logout',
                icon: <LogoutOutlined />,
                label: 'Đăng xuất',
                onClick: handleLogout
              }
            ]}
          />
        </div>
      </Sider>
      <Layout>
        <Header className="admin-header">
          <h1>Quản trị hệ thống</h1>
        </Header>
        <Content className="admin-content">
          {children}
        </Content>
      </Layout>
    </Layout>
  );
};

export default DashboardAdmin;