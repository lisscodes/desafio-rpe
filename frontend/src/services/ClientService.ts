import axios from 'axios';
import { Client } from '../models/Client';

const API_URL = 'http://localhost:8080/clientes';

export const clientService = {
  async listarTodos(): Promise<Client[]> {
    const response = await axios.get<Client[]>(API_URL);
    return response.data;
  },

  async buscarPorId(id: number): Promise<Client> {
    const response = await axios.get<Client>(`${API_URL}/${id}`);
    return response.data;
  },

  async listarBloqueados(): Promise<Client[]> {
    const response = await axios.get<Client[]>(`${API_URL}/bloqueados`);
    return response.data;
  },
};
