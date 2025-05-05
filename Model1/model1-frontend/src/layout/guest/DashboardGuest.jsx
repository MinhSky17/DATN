import { useEffect, useState } from "react";
// import FooterGuest from "../../component/guest/FooterGuest";
// import HeaderGuest from "../../component/guest/HeaderGuest";
import "./style-dashboard-guest.css";
import Logo from "../../assets/img/logo.jpeg";

const DashboardGuest = ({ children }) => {
  const scrollToTop = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const [isVisible, setIsVisible] = useState(false);

  const toggleVisibility = () => {
    if (window.pageYOffset > 50) {
      setIsVisible(true);
    } else {
      setIsVisible(false);
    }
  };

  useEffect(() => {
    window.addEventListener("scroll", toggleVisibility);
    return () => {
      window.removeEventListener("scroll", toggleVisibility);
    };
  }, []);
  return (
    <div>
      <div
        style={{
          position: "fixed",
          bottom: 20,
          right: 20,
          zIndex: 500,
          cursor: "pointer",
          backgroundColor: "white",
          outline: "none",
          border: "1px solid rgba(148, 148, 148, 0.3)",
          borderRadius: "50%",
          boxShadow: "0px 0px 20px 1px rgba(148, 148, 148, 0.3)",
          transition: "0.5s",
          transform: isVisible ? "translateY(0)" : "translateY(50px)",
          opacity: isVisible ? 1 : 0,
        }}
        onClick={scrollToTop}
        role="button"
        aria-label="Scroll to top"
      >
        <img
          src={Logo}
          alt="To Top"
          width={40}
          style={{ borderRadius: "50%" }}
        />
      </div>
      {/* <HeaderGuest></HeaderGuest> */}
      {children}
      {/* <FooterGuest></FooterGuest> */}
    </div>
  );
};

export default DashboardGuest;
