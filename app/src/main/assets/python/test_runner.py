import unittest
import importlib.util
import sys
import os

def run_test(user_code_path, test_filename):
    try:
        # cargar código como módulo
        spec = importlib.util.spec_from_file_location("user_code", user_code_path)
        user_code = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(user_code)
        sys.modules["user_code"] = user_code

        # cargar archivo de prueba
        test_dir = os.path.dirname(__file__)
        sys.path.insert(0, test_dir)
        test_module = importlib.import_module(test_filename.replace(".py", ""))

        suite = unittest.defaultTestLoader.loadTestsFromModule(test_module)
        result = unittest.TextTestRunner(stream=open(os.devnull, "w")).run(suite)

        if result.wasSuccessful():
            return f"Todos los tests fueron pasados con éxito ({result.testsRun}/{result.testsRun})"
        else:
            failures = "\n".join([f"{f[0]}: {f[1]}" for f in result.failures])
            return f"{len(result.failures)} test(s) fallaron:\n{failures}"
    except Exception as e:
        return f"Error al ejecutar pruebas: {str(e)}"