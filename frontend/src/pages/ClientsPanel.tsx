import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ClientsList from '../components/ClientsList';
import { Cliente } from '../models/Client';
import { clienteService } from '../services/ClientService';

const ClientsPanel = () => {
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const navigate = useNavigate();

  useEffect(() => {
    clienteService.listarTodos()
      .then(setClientes)
      .catch(err => console.error('Erro ao buscar clientes:', err));
  }, []);

  const handleVerFaturas = (id: number) => {
    navigate(`/invoices/${id}`);
  };

  return (
    <div className="max-w-6xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">Painel de Clientes</h1>
      <ClientsList clientes={clientes} onVerFaturas={handleVerFaturas} />
    </div>
  );
};

export default ClientsPanel;
