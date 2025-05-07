import unittest
from unittest.mock import patch
from user_code import ordenar_parrafo

class TestOrdenarParrafo(unittest.TestCase):
	@patch("builtins.input", return_value="la casa es grande")
	def test_ordenamiento_basico(self, mock_input):
	    resultado = ordenar_parrafo()
	    self.assertEqual(resultado, "casa es grande la")

	@patch("builtins.input", return_value="zorro águila burro")
	def test_con_palabras_con_acentos(self, mock_input):
	    resultado = ordenar_parrafo()
	    self.assertEqual(resultado, "burro zorro águila")  # Python usa orden unicode

	@patch("builtins.input", return_value="sol luna estrella")
	def test_orden_al_reves(self, mock_input):
	    resultado = ordenar_parrafo()
	    self.assertEqual(resultado, "estrella luna sol")

	@patch("builtins.input", return_value="")
	def test_parrafo_vacio(self, mock_input):
	    resultado = ordenar_parrafo()
	    self.assertEqual(resultado, "")

if __name__ == '__main__':
    unittest.main()