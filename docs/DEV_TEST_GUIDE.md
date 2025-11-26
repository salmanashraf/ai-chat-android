# Developer & Tester Guide

## Sample App Flow
1. Launch the `sampleapp` module.
2. Use the provider dropdown (spinner) to pick OpenAI, Gemini, Claude, or Grok.
3. Enter the credential for the selected provider:
   - OpenAI/Claude/Grok: paste the API key.
   - Gemini: Either paste the API key **or** copy your `google.json` service file into `sampleapp/src/main/assets/google.json` (preferred). The sample automatically reads that file when you tap Apply.
4. Tap **Apply** to reconfigure the SDK. The chat surface below immediately uses that provider.
5. Repeat the process to switch providers or update keys; previously entered keys persist in the text field.

## Notes
- Keys are kept in-memory for the session only; restart clears them.
- The chat area will show errors if you try to converse without applying a provider with a key.
- Use the Config card to validate each provider quickly before running integration tests.
