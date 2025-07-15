import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './pages/HomePage';
import ClientsPanel from './pages/ClientsPanel';
import InvoicesPage from './pages/InvoicesPage';
import NewClientPage from './pages/NewClientPage';

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/clients" element={<ClientsPanel />} />
        <Route path="/invoices/:id" element={<InvoicesPage />} />
        <Route path="/clients/new" element={<NewClientPage />} />
      </Routes>
    </>
  );
}

export default App;
