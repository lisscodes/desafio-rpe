import { Link } from "react-router-dom";
import { User } from "lucide-react";

export default function Header() {
  return (
    <nav className="bg-gray-800 text-white p-4 shadow-md">
      <div className="container mx-auto flex items-center justify-between">
        <div className="flex items-center space-x-10">
          <span className="text-xl font-bold">Desafio RPE</span>
          <div className="flex space-x-10">
            <Link to="/" className="hover:text-gray-300">Início</Link>
            <Link to="" className="hover:text-gray-300">Financeiro</Link>
            <Link to="" className="hover:text-gray-300">Cadastro</Link>
          </div>
        </div>

        <div>
          <Link to="/perfil" className="hover:text-gray-300" title="Perfil">
            <User className="w-6 h-6" />
          </Link>
        </div>
      </div>
    </nav>
  );
}
