import { Suspense } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "./App.css";
import { AppConfig } from "./AppConfig";
import { useAppSelector } from "./app/hook";
import { GetLoading } from "./app/reducer/common/LoadingSlice.reducer.js";
import LoadingHamster from "./helper/loading/loading-hamster.js";
import routes from "./router/router.js";


function App() {
  const isLoading = useAppSelector(GetLoading);

  return (
    <div className="App scroll-smooth md:scroll-auto"> 
      <ToastContainer />
      {isLoading && <LoadingHamster />}
      <BrowserRouter basename={AppConfig.routerBase}>
        <Suspense>
          <Routes>
            {routes.map((route, index) => (
              <Route key={index} path={route.path} element={route.element} />
            ))}
          </Routes>
        </Suspense>
      </BrowserRouter>
    </div>
  );
}

export default App;
