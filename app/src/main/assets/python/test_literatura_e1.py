import unittest
from unittest.mock import patch
from user_code import contar_palabras

class TestContadorPalabras(unittest.TestCase):
	@patch('builtins.input', return_value="Hola mundo hola")
	def test_contar_palabras_basico(self, mock_input):
	    resultado = contar_palabras()
	    esperado = {'hola': 2, 'mundo': 1}
	    self.assertEqual(resultado, esperado)

	@patch('builtins.input', return_value="Python Python PYTHON")
	def test_palabras_con_mayusculas(self, mock_input):
	    resultado = contar_palabras()
	    esperado = {'python': 3}
	    self.assertEqual(resultado, esperado)

	@patch('builtins.input', return_value="uno uno uno dos dos tres")
	def test_varias_repeticiones(self, mock_input):
	    resultado = contar_palabras()
	    esperado = {'uno': 3, 'dos': 2, 'tres': 1}
	    self.assertEqual(resultado, esperado)

if __name__ == '__main__':
    unittest.main()