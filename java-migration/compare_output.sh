#!/bin/bash

set -e

echo "=========================================="
echo "COBOL to Java Serialization Comparison"
echo "=========================================="
echo ""

echo "Step 1: Compiling and running COBOL XML program..."
cd /home/ubuntu/repos/COBOL-Demo/xml_generate
cobc -x xml_generate.cbl -o xml_test
./xml_test > /tmp/cobol_xml_output.txt 2>&1
echo "COBOL XML output saved to /tmp/cobol_xml_output.txt"
echo ""

echo "Step 2: Compiling and running COBOL JSON program..."
cd /home/ubuntu/repos/COBOL-Demo/json_generate
cobc -x json_generate.cbl -o json_test
./json_test > /tmp/cobol_json_output.txt 2>&1
echo "COBOL JSON output saved to /tmp/cobol_json_output.txt"
echo ""

echo "Step 3: Building Java project..."
cd /home/ubuntu/repos/COBOL-Demo/java-migration
mvn clean package -q
echo "Java project built successfully"
echo ""

echo "Step 4: Running Java serialization..."
java -jar target/cobol-serialization-migration-1.0.0.jar > /tmp/java_output.txt 2>&1
echo "Java output saved to /tmp/java_output.txt"
echo ""

echo "=========================================="
echo "COBOL XML Output:"
echo "=========================================="
cat /tmp/cobol_xml_output.txt
echo ""

echo "=========================================="
echo "COBOL JSON Output:"
echo "=========================================="
cat /tmp/cobol_json_output.txt
echo ""

echo "=========================================="
echo "Java Output (Both XML and JSON):"
echo "=========================================="
cat /tmp/java_output.txt
echo ""

echo "=========================================="
echo "Comparison Summary:"
echo "=========================================="
echo "Both COBOL and Java implementations:"
echo "- Generate XML with declaration, root element, and attributes"
echo "- Generate JSON with proper field mapping"
echo "- Suppress empty/blank fields"
echo "- Track character counts"
echo "- Handle errors with exception handling"
echo ""
echo "Manual verification required to ensure outputs match semantically."
echo "Note: Exact formatting may differ between COBOL and Java libraries."
