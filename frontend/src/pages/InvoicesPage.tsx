import React from 'react';
import { useParams } from 'react-router-dom';
import InvoiceList from '../components/InvoiceList';

const InvoicesPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();

  return (
    <div className="max-w-6xl mx-auto mt-8 px-4">
      <h1 className="text-2xl font-bold mb-4">Faturas do Cliente #{id}</h1>
      <InvoiceList clienteId={id} />
    </div>
  );
};

export default InvoicesPage;
