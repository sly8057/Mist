import unittest
from unittest.mock import patch
from user_code import pedir_y_calcular_pasos

class TestPasosConInput(unittest.TestCase):

    @patch('builtins.input', side_effect=['1000', '2000', '3000', '4000', '5000', '6000', '7000'])
    def test_pedir_y_calcular(self, mock_input):
        total, promedio = pedir_y_calcular_pasos()
        self.assertEqual(total, 28000)
        self.assertEqual(promedio, 4000.0)

if __name__ == '__main__':
    unittest.main()