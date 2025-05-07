import unittest
from unittest.mock import patch
from user_code import juego_camino

class TestJuegoCamino(unittest.TestCase):
	@patch('builtins.input', return_value='izquierda')
	def test_camino_izquierda(self, mock_input):
	    resultado = juego_camino()
	    self.assertEqual(resultado, "Encontraste un cofre del tesoro.")

	@patch('builtins.input', return_value='derecha')
	def test_camino_derecha(self, mock_input):
	    resultado = juego_camino()
	    self.assertEqual(resultado, "Caíste en una trampa.")

	@patch('builtins.input', return_value='arriba')
	def test_camino_invalido(self, mock_input):
	    resultado = juego_camino()
	    self.assertEqual(resultado, "No elegiste un camino válido.")

if __name__ == '__main__':
    unittest.main()