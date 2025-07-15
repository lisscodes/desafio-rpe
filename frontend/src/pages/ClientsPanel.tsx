import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ClientsList from '../components/ClientsList';
import { Client } from '../models/Client';
import { clientService } from '../services/ClientService';
import { Search, Ban, RefreshCcw } from 'lucide-react';

const ClientsPanel = () => {
  const [clientes, setClientes] = useState<Client[]>([]);
  const [buscaId, setBuscaId] = useState('');
  const navigate = useNavigate();

  const carregarTodosClientes = () => {
    clientService.listarTodos()
      .then(setClientes)
      .catch(err => console.error('Erro ao buscar clientes:', err));
  };

  useEffect(() => {
    carregarTodosClientes();
  }, []);

  const handleVerFaturas = (id: number) => {
    navigate(`/invoices/${id}`);
  };

  const handleBuscarPorId = () => {
    if (!buscaId.trim()) return;

    clientService.buscarPorId(Number(buscaId))
      .then(cliente => setClientes([cliente]))
      .catch(err => {
        console.error('Cliente não encontrado:', err);
        setClientes([]);
      });
  };

  const handleBuscarBloqueados = () => {
    clientService.listarBloqueados()
      .then(setClientes)
      .catch(err => console.error('Erro ao buscar bloqueados:', err));
  };

  return (
    <div className="max-w-6xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">Painel de Clientes</h1>

      {/* Filtros de busca */}
      <div className="flex flex-col md:flex-row gap-2 mb-4">
        <input
          type="number"
          placeholder="Buscar cliente por ID..."
          className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
          value={buscaId}
          onChange={(e) => setBuscaId(e.target.value)}
        />
        <button
          onClick={handleBuscarPorId}
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center gap-2"
        >
          <Search size={18} />
          Buscar por ID
        </button>
        <button
          onClick={handleBuscarBloqueados}
          className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg flex items-center gap-2"
        >
          <Ban size={18} />
          Bloqueados
        </button>
        <button
          onClick={carregarTodosClientes}
          className="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-lg flex items-center gap-2"
        >
          <RefreshCcw size={18} />
          Limpar filtros
        </button>
      </div>

      <ClientsList clientes={clientes} onVerFaturas={handleVerFaturas} />
    </div>
  );
};

export default ClientsPanel;
