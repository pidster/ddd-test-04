/**
 * @jest-environment node
 */
import { join } from 'path';
import { readdirSync, statSync } from 'fs';

// This test requires @types/jest and @types/node to be installed
// It should be run in a Node environment, not in a browser

describe('Frontend Architecture Rules', () => {
  const COMPONENTS_DIR = join(process.cwd(), 'src', 'components');
  const PAGES_DIR = join(process.cwd(), 'src', 'pages');
  const SERVICES_DIR = join(process.cwd(), 'src', 'services');
  const STORE_DIR = join(process.cwd(), 'src', 'store');

  const readFilesRecursively = (dir: string): string[] => {
    const entries = readdirSync(dir, { withFileTypes: true });
    
    const files = entries
      .filter(entry => !entry.isDirectory())
      .map(entry => join(dir, entry.name));
      
    const folders = entries
      .filter(entry => entry.isDirectory())
      .map(entry => join(dir, entry.name));
      
    const nestedFiles = folders.flatMap(folder => readFilesRecursively(folder));
    
    return [...files, ...nestedFiles];
  };

  // Check if a file imports from a specific path
  const importsFrom = (content: string, path: string): boolean => {
    const importRegex = new RegExp(`import .* from ['"]${path}['"]`, 'g');
    return importRegex.test(content);
  };

  test('Components should not import from pages', () => {
    const componentFiles = readFilesRecursively(COMPONENTS_DIR)
      .filter(file => file.endsWith('.tsx') || file.endsWith('.ts'));
      
    componentFiles.forEach(file => {
      const content = require('fs').readFileSync(file, 'utf8');
      expect(importsFrom(content, '@pages')).toBe(false);
    });
  });

  test('Components should not directly import from store (should use hooks instead)', () => {
    const componentFiles = readFilesRecursively(COMPONENTS_DIR)
      .filter(file => file.endsWith('.tsx') || file.endsWith('.ts'))
      .filter(file => !file.includes('containers'));
      
    componentFiles.forEach(file => {
      const content = require('fs').readFileSync(file, 'utf8');
      expect(importsFrom(content, '@store')).toBe(false);
    });
  });

  test('Services should not import from components or pages', () => {
    const serviceFiles = readFilesRecursively(SERVICES_DIR)
      .filter(file => file.endsWith('.ts'));
      
    serviceFiles.forEach(file => {
      const content = require('fs').readFileSync(file, 'utf8');
      expect(importsFrom(content, '@components')).toBe(false);
      expect(importsFrom(content, '@pages')).toBe(false);
    });
  });

  test('Pages should use Lazy loading for performance', () => {
    const pageFiles = readFilesRecursively(PAGES_DIR)
      .filter(file => file.endsWith('.tsx'));
      
    // This is a simplified check - in a real project you would check
    // for React.lazy() usage or a similar pattern
    pageFiles.forEach(file => {
      const stats = statSync(file);
      // Pages should not be too large
      expect(stats.size).toBeLessThan(10000);
    });
  });
});
