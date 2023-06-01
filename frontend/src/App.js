import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { NavigationBar } from './components';
import Pages from './pages';

function App() {
  return (
    <Router>
      <NavigationBar />
      <Routes>
        {Object.entries(Pages).map(([_, page]) => <Route key={page.name} exact path={page.path} element={page.component} />)}
      </Routes>
    </Router>
  );
}

export default App;
