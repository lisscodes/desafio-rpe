import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { clientService } from '../services/ClientService';

const NewClientPage = () => {
  const [nome, setNome] = useState('');
  const [cpf, setCpf] = useState('');
  const [dataNascimento, setDataNascimento] = useState('');
  const [limiteCredito, setLimiteCredito] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await clientService.criar({
        nome,
        cpf,
        dataNascimento,
        limiteCredito: parseFloat(limiteCredito)
      });
      alert('Cliente cadastrado com sucesso!');
      navigate('/clients');
    } catch (error) {
      alert('Erro ao cadastrar cliente.');
      console.error(error);
    }
  };

  return (
    <div className="max-w-xl mx-auto mt-10 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Cadastrar Novo Cliente</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        <input
          type="text"
          placeholder="Nome"
          className="w-full p-2 border rounded"
          value={nome}
          onChange={(e) => setNome(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="CPF"
          className="w-full p-2 border rounded"
          value={cpf}
          onChange={(e) => setCpf(e.target.value)}
          required
        />
        <input
          type="date"
          className="w-full p-2 border rounded"
          value={dataNascimento}
          onChange={(e) => setDataNascimento(e.target.value)}
          required
        />
        <input
          type="number"
          step="0.01"
          placeholder="Limite de Crédito"
          className="w-full p-2 border rounded"
          value={limiteCredito}
          onChange={(e) => setLimiteCredito(e.target.value)}
          required
        />
        <button
          type="submit"
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
        >
          Cadastrar
        </button>
      </form>
    </div>
  );
};

export default NewClientPage;
