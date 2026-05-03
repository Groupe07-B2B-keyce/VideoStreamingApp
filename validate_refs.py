import yaml
import os
import re

def validate_refs(file_path, base_dir="."):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Find all $ref: '...' or $ref: "..." or $ref: path
    refs = re.findall(r"\$ref:\s*['\"]?([^'\"\s]+)['\"]?", content)
    errors = []
    
    for ref in refs:
        if ref.startswith('#'): continue # Internal ref, skip for now
        
        parts = ref.split('#')
        file_part = parts[0]
        
        target_path = os.path.normpath(os.path.join(os.path.dirname(file_path), file_part))
        if not os.path.exists(target_path):
            errors.append(f"In {file_path}: Broken file reference -> {target_path} from {ref}")
            
    return errors

all_errors = []
def scan_dir(d):
    for root, dirs, files in os.walk(d):
        for file in files:
            if file.endswith(".yaml"):
                all_errors.extend(validate_refs(os.path.join(root, file)))

scan_dir("openapi")

if all_errors:
    print("Errors found:")
    for e in all_errors:
        print(e)
else:
    print("All file references appear to exist.")
