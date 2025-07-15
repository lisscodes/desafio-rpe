import axios from 'axios';
import { Cliente } from '../models/Client';

const API_URL = 'http://localhost:8080/clientes';

export const clienteService = {
  async listarTodos(): Promise<Cliente[]> {
    const response = await axios.get<Cliente[]>(API_URL);
    return response.data;
  },

  async buscarPorId(id: number): Promise<Cliente> {
    const response = await axios.get<Cliente>(`${API_URL}/${id}`);
    return response.data;
  },

  async listarBloqueados(): Promise<Cliente[]> {
    const response = await axios.get<Cliente[]>(`${API_URL}/bloqueados`);
    return response.data;
  },
};
