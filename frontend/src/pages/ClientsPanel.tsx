import React from 'react';
import { useNavigate } from 'react-router-dom';
import ClientsList from '../components/ClientsList';

const clientes = [
  { id: 1, nome: 'José Silva', cpf: '123.456.789-09', idade: 45, bloqueado: false, limiteCredito: 5000 },
  { id: 2, nome: 'Ana Souza', cpf: '987.654.821-01', idade: 38, bloqueado: false, limiteCredito: 10000 },
  { id: 3, nome: 'Carlos Lima', cpf: '111.222.333-44', idade: 51, bloqueado: true, limiteCredito: 7500 },
  { id: 4, nome: 'Marta Alves', cpf: '222.333.444-55', idade: 29, bloqueado: false, limiteCredito: 3000 },
];

const PainelDeClientes = () => {
  const navigate = useNavigate();

  const handleVerFaturas = (clienteId: number) => {
    navigate(`/invoices/${clienteId}`);
  };

  return (
    <div className="max-w-6xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold text-gray-800 mb-4">Painel de Clientes</h1>
      <ClientsList clientes={clientes} onVerFaturas={handleVerFaturas} />
    </div>
  );
};

export default PainelDeClientes;
