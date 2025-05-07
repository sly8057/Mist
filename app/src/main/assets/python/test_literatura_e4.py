import unittest
from unittest.mock import patch
from user_code import generador_rimas

class TestGeneradorRimas(unittest.TestCase):
	@patch("builtins.input", return_value="taza")
	def test_rima_za(self, mock_input):
	    resultado = generador_rimas()
	    self.assertIn("plaza", resultado)
	    self.assertIn("casa", resultado)
	    self.assertNotIn("mesa", resultado)

	@patch("builtins.input", return_value="pisa")
	def test_rima_sa(self, mock_input):
	    resultado = generador_rimas()
	    self.assertIn("risa", resultado)
	    self.assertIn("camisa", resultado)
	    self.assertNotIn("casa", resultado)

	@patch("builtins.input", return_value="sol")
	def test_sin_rimas(self, mock_input):
	    resultado = generador_rimas()
	    self.assertEqual(resultado, [])

	@patch("builtins.input", return_value="mesa")
	def test_rima_sa_exacto(self, mock_input):
	    resultado = generador_rimas()
	    self.assertIn("mesa", resultado)
	    self.assertIn("risa", resultado)
	    self.assertIn("camisa", resultado)

if __name__ == '__main__':
    unittest.main()