import React from 'react';
import { useNavigate } from 'react-router-dom';

const HomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-[#f8f8f8] flex flex-col px-4 pb-8">
      {/* Logo and banners */}
      <div className="flex justify-center items-center bg-white py-6">
        <div className="w-1/4 h-28 bg-white mr-4 rounded" />
        <img src="/assets/logo.png" alt="Logo RPE" className="h-28 object-contain" />
        <div className="w-1/4 h-28 bg-white ml-4 rounded" />
      </div>

      {/* Financeiro Section */}
      <section className="bg-[#f1eaea] my-6 p-4 rounded shadow-md">
        <h2 className="text-xl font-bold text-gray-800 mb-4">Financeiro</h2>
        <div className="grid grid-cols-4 gap-4">
          <div
            className="relative cursor-pointer group"
            onClick={() => navigate('/clients')}
          >
            <img
              src="/assets/fluxo-caixa.jpg"
              alt="Fluxo de caixa"
              className="rounded shadow w-full h-32 object-cover group-hover:brightness-90 transition"
            />
            <span className="absolute bottom-2 left-2 text-white font-bold text-lg bg-black/50 px-2 py-1 rounded">
              Painel de Clientes
            </span>
          </div>
          <div className="bg-gray-100 rounded shadow h-32" />
          <div className="bg-gray-100 rounded shadow h-32" />
          <div className="bg-gray-100 rounded shadow h-32" />
        </div>
      </section>

      {/* Cadastro Section */}
      <section className="bg-[#f1eaea] my-6 p-4 rounded shadow-md">
        <h2 className="text-xl font-bold text-gray-800 mb-4">Cadastro</h2>
        <div className="grid grid-cols-4 gap-4">
          <div
            className="relative cursor-pointer group"
            onClick={() => navigate('')}
          >
            <img
              src="/assets/novos-clientes.png"
              alt="Novos Clientes"
              className="rounded shadow w-full h-32 object-cover group-hover:brightness-90 transition"
            />
            <span className="absolute bottom-2 left-2 text-white font-bold text-lg bg-black/50 px-2 py-1 rounded">
              Novos Clientes
            </span>
          </div>
          <div className="bg-gray-100 rounded shadow h-32" />
          <div className="bg-gray-100 rounded shadow h-32" />
        </div>
      </section>
    </div>
  );
};

export default HomePage;
