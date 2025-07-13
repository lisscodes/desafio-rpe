import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import ClientsPanel from './pages/ClientsPanel';
import InvoicesPage from './pages/InvoicesPage';

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/clients" element={<ClientsPanel />} />
        <Route path="/invoices/:id" element={<InvoicesPage />} />
      </Routes>
    </>
  );
}

export default App;
