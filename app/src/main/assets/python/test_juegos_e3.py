import unittest
from unittest.mock import patch
from user_code import lanzar_dado

class TestLanzarDado(unittest.TestCase):
	@patch('random.randint', return_value=4)
	def test_lanzamiento_fijo(self, mock_randint):
	    resultado = lanzar_dado()
	    self.assertEqual(resultado, 4)

	def test_valor_en_rango(self):
	    for _ in range(20):
	        resultado = lanzar_dado()
	        self.assertTrue(1 <= resultado <= 6)

if __name__ == '__main__':
    unittest.main()