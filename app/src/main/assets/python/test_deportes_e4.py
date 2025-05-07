import unittest
from unittest.mock import patch
from user_code import calcular_imc_desde_input

class TestIMCConInput(unittest.TestCase):

    @patch('builtins.input', side_effect=['45', '1.70'])
    def test_bajo_peso(self, mock_input):
        imc, categoria = calcular_imc_desde_input()
        self.assertEqual(categoria, "Bajo peso")
        self.assertAlmostEqual(imc, 15.57, places=2)

    @patch('builtins.input', side_effect=['65', '1.70'])
    def test_normal(self, mock_input):
        imc, categoria = calcular_imc_desde_input()
        self.assertEqual(categoria, "Normal")
        self.assertAlmostEqual(imc, 22.49, places=2)

    @patch('builtins.input', side_effect=['78', '1.70'])
    def test_sobrepeso(self, mock_input):
        imc, categoria = calcular_imc_desde_input()
        self.assertEqual(categoria, "Sobrepeso")
        self.assertAlmostEqual(imc, 26.99, places=2)

    @patch('builtins.input', side_effect=['95', '1.70'])
    def test_obesidad(self, mock_input):
        imc, categoria = calcular_imc_desde_input()
        self.assertEqual(categoria, "Obesidad")
        self.assertAlmostEqual(imc, 32.87, places=2)

if __name__ == '__main__':
    unittest.main()