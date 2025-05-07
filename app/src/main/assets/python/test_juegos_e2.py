import unittest
from unittest.mock import patch
from user_code import adivina_numero

class TestAdivinaNumero(unittest.TestCase):
	@patch('builtins.input', side_effect=['30', '70', '50'])
	@patch('random.randint', return_value=50)
	def test_adivina_correctamente_en_tres_intentos(self, mock_randint, mock_input):
	    intentos = adivina_numero()
	    self.assertEqual(intentos, 3)

	@patch('builtins.input', side_effect=['50'])
	@patch('random.randint', return_value=50)
	def test_adivina_correctamente_en_un_intento(self, mock_randint, mock_input):
	    intentos = adivina_numero()
	    self.assertEqual(intentos, 1)

if __name__ == '__main__':
    unittest.main()