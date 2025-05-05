import React, {useState} from 'react';
import { Button, Carousel, Card, Input, Modal } from 'antd';
import { SearchOutlined, UserOutlined, ShoppingCartOutlined } from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import './style.css';
import logo from '../../assets/img/logo.jpeg';
import Login from './Login';

const Header = () => {
    const [isVisibleModalLogin, setIsVisibleModalLogin] = useState(false);

    const openModalLogin = () => {
      setIsVisibleModalLogin(true);
    };
  
    const cancelModalLogin = () => {
      setIsVisibleModalLogin(false);
    };
  return (
    <>
      <header className="header">
        <div className="header-content">
          <div className="logo-section">
            <img src={logo} alt="Logo" />
            <span className="brand-name">CoShine.VN</span>
          </div>
          
          <div className="search-section">
            <Input 
              placeholder="Tìm kiếm sản phẩm..." 
              prefix={<SearchOutlined />} 
              size="large"
            />
          </div>

          <div className="user-actions">
            <div className="login-btn" onClick={openModalLogin}>
              <UserOutlined />
              <span>Đăng nhập</span>
            </div>
            <div className="cart-btn">
              <ShoppingCartOutlined />
              <span className="badge">0</span>
            </div>
          </div>
        </div>
      </header>

      {/* <Modal
        open={isVisibleModalLogin}
        onCancel={cancelModalLogin}
        footer={null}
        destroyOnClose={true}
      >
        <Login onCancel={cancelModalLogin} />
      </Modal> */}
      <Login isVisible = {isVisibleModalLogin} onCancel={cancelModalLogin} />

      <nav className="nav-menu">
        <div className="nav-content">
          <ul className="nav-list">
            <li className="nav-item">
              <Link to="/">Trang chủ</Link>
            </li>
            <li className="nav-item">
              <Link to="/makeup">Trang điểm</Link>
            </li>
            <li className="nav-item">
              <Link to="/skincare">Chăm sóc</Link>
            </li>
            <li className="nav-item">
              <Link to="/news">Tin tức</Link>
            </li>
          </ul>
        </div>
      </nav>
    </>
  );
};

const Home = () => {
  
  
  const banners = [
    { image: '/images/banner1.jpg', alt: 'Banner 1' },
    { image: '/images/banner2.jpg', alt: 'Banner 2' },
    { image: '/images/banner3.jpg', alt: 'Banner 3' },
  ];

  const hotBrands = [
    { name: 'MAC', image: '/images/brands/mac.png' },
    { name: 'Lancome', image: '/images/brands/lancome.png' },
    { name: 'Estee Lauder', image: '/images/brands/estee.png' },
    { name: 'SK-II', image: '/images/brands/skii.png' },
  ];

  return (
    <>
      <Header />
      <div className="home">
        <Carousel autoplay className="banner-carousel">
          {banners.map((banner, index) => (
            <div key={index}>
              <img src={banner.image} alt={banner.alt} />
            </div>
          ))}
        </Carousel>
  
        <section className="hot-brands">
          <h2>Thương hiệu nổi bật</h2>
          <div className="brands-grid">
            {hotBrands.map((brand, index) => (
              <div key={index} className="brand-card">
                <img src={brand.image} alt={brand.name} />
              </div>
            ))}
          </div>
        </section>
  
        <section className="categories">
          <h2>Danh mục sản phẩm</h2>
          <div className="categories-grid">
            <Card className="category-card" hoverable cover={<img src="/images/makeup.jpg" alt="Trang điểm" />}>
              <h3>Trang điểm</h3>
            </Card>
            <Card className="category-card" hoverable cover={<img src="/images/skincare.jpg" alt="Chăm sóc da" />}>
              <h3>Chăm sóc da</h3>
            </Card>
            <Card className="category-card" hoverable cover={<img src="/images/perfume.jpg" alt="Nước hoa" />}>
              <h3>Nước hoa</h3>
            </Card>
            <Card className="category-card" hoverable cover={<img src="/images/bodycare.jpg" alt="Chăm sóc cơ thể" />}>
              <h3>Chăm sóc cơ thể</h3>
            </Card>
          </div>
        </section>
  
        <section className="best-sellers">
          <h2>Sản phẩm bán chạy</h2>
          <div className="products-grid">
            {[1, 2, 3, 4, 5, 6].map((item) => (
              <Card key={item} className="product-card" hoverable>
                <img src={`/images/product${item}.jpg`} alt={`Product ${item}`} />
                <div className="product-info">
                  <h3>Sản phẩm {item}</h3>
                  <p className="brand">Thương hiệu</p>
                  <p className="price">890.000đ</p>
                  <Button type="primary">Thêm vào giỏ</Button>
                </div>
              </Card>
            ))}
          </div>
        </section>
  
        <section className="new-arrivals">
          <h2>Sản phẩm mới</h2>
          <Carousel autoplay dots={false} slidesToShow={4} className="products-carousel">
            {[1, 2, 3, 4, 5, 6, 7, 8].map((item) => (
              <Card key={item} className="product-card" hoverable>
                <img src={`/images/new${item}.jpg`} alt={`New Product ${item}`} />
                <div className="product-info">
                  <h3>Sản phẩm mới {item}</h3>
                  <p className="brand">Thương hiệu</p>
                  <p className="price">590.000đ</p>
                </div>
              </Card>
            ))}
          </Carousel>
        </section>
      </div>
    </>
  );
};

export default Home;
